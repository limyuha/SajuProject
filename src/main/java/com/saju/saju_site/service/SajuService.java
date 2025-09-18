package com.saju.saju_site.service;

import com.saju.saju_site.service.client.OpenAiClient;
import com.saju.saju_site.web.dto.SajuRequest;
import org.springframework.stereotype.Service;

@Service
public class SajuService {
    private final OpenAiClient client;

    public SajuService(OpenAiClient client) {
        this.client = client;
    }

    public String analyze(SajuRequest req) {
        String prompt = """
너는 한국 전통 사주 전문가다.
아래 사용자가 입력한 사주 정보를 바탕으로 실제 운세 풀이를 해줘.

[사주 정보]
- 이름: %s
- 생년월일: %s
- 성별: %s
- 태어난 시각(시주): %s

[사용자 질문]
%s

[요청 사항]
- 반드시 한국어로 답변해.
- 결과를 **마크다운 형식**으로 작성해.
- 태어난 시각은 'HH:mm' 형태나 '자·축·인·묘…' 같은 전통 시각 표기 중 하나로 입력될 수 있어.
- 사용자가 태어난 시각을 모른다고 입력한 경우, 시각 없는 풀이를 진행해.
- 불필요한 원리 설명은 생략하고, 실제 운세 풀이에 집중해.
- 구체적이고 친절한 말투로 설명해.
- 현재 날짜 시각 기준으로 대답해줘(현재 날짜는 2025년 9월)
- 과거와 현재, 미래까지 말해주면 좋아.
- 긍정적인 부분만 말하지말고 부정적이거나 조심해야할부분도 같이 말해줘.
- 사용자가 입력한 정보(기본 사주 정보 같은)는 이미 출력하게 되어 있기 때문에 답변 하지 않아도 돼.
""".formatted(
                req.getName(),
                req.getBirth(),
                req.getGender(),
                (req.getTime() == null || req.getTime().isBlank()) ? "모름" : req.getTime(),
                req.getQuestion()
        );
        return client.getSajuResult(prompt);
    }
}

//- 구조적으로 보기 좋게 `### 제목`, `- 리스트`, `**강조**` 등을 활용해라.

