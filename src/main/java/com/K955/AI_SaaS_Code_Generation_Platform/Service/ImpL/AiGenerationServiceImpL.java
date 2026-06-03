package com.K955.AI_SaaS_Code_Generation_Platform.Service.ImpL;

import com.K955.AI_SaaS_Code_Generation_Platform.LLM.PromptUtils;
import com.K955.AI_SaaS_Code_Generation_Platform.Security.JwtAuthUtil;
import com.K955.AI_SaaS_Code_Generation_Platform.Service.AiGenerationService;
import com.K955.AI_SaaS_Code_Generation_Platform.Service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiGenerationServiceImpL implements AiGenerationService {

    private final ChatClient chatClient;
    private final JwtAuthUtil jwtAuthUtil;
    private final ProjectFileService projectFileService;

    private static final Pattern FILE_TAG_PATTERN = Pattern.compile("<file path=\"([^\"]+)\">(.*?)</file>", Pattern.DOTALL);

    @Override
    @PreAuthorize("@security.canEditProject(#projectId)") //check if the user can edit the project or not
    public Flux<String> streamResponse(String userMessage, Long projectId) {
        Long userId = jwtAuthUtil.getCurrentUserId();

        createChatSessionIfNotExists(projectId, userId);

        Map<String, Object> advisorParams = Map.of(
                "userId", userId,
                "projectId", projectId
        );

        StringBuilder fullResponseBuffer = new StringBuilder();

        return chatClient.prompt()
                .system(PromptUtils.CODE_GENERATION_SYSTEM_PROMPT)
                .user(userMessage)
                .advisors(
                        advisorSpec -> {
                            advisorSpec.params(advisorParams);
                        }
                )
                .stream()
                .chatResponse()
                .doOnNext(response -> { //while streaming the res, next step - appending chunks
                    String content = response.getResult().getOutput().getText();
                    fullResponseBuffer.append(content);
                })
                .doOnComplete(() -> { //when streaming ends
                    Schedulers.boundedElastic().schedule(() -> {
                        parseAndSaveFiles(fullResponseBuffer.toString(), projectId); //will be executed by a new thread now
                    });
                })
                .doOnError(error -> log.error("Error during streaming for projectId: {}", projectId))
                .map(response -> response.getResult().getOutput().getText());
    }

    private void parseAndSaveFiles(String fullResponse, Long projectId) {
//        String dummy = """
//                        <message> I'm going to read the files and generate the code </message>
//                        <file path="src/App.jsx">
//                            import App from './App.jsx'
//                            ......
//                        <file>
//                        <message> I'm going to read the files and generate the code </message>
//                        <file path="src/App.jsx">
//                            import App from './App.jsx'
//                            ......
//                        <file>
//                        """
        Matcher matcher = FILE_TAG_PATTERN.matcher(fullResponse);
        while(matcher.find()) {
            String filePath = matcher.group(1);
            String fileContent = matcher.group(2).trim();

            projectFileService.saveFile(projectId, filePath, fileContent);
        }

    }

    private void createChatSessionIfNotExists(Long projectId, Long userId) {

    }

}
