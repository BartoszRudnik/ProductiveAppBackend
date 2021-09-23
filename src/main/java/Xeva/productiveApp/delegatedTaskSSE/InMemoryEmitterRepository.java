package Xeva.productiveApp.delegatedTaskSSE;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Repository
public class InMemoryEmitterRepository implements EmitterRepository{
    private final Map<String, SseEmitter> userEmitterMap = new ConcurrentHashMap<>();

    @Override
    public void addOrReplaceEmitter(String memberId, SseEmitter emitter) {
        this.userEmitterMap.put(memberId, emitter);
    }

    @Override
    public void remove(String memberId) {
        this.userEmitterMap.remove(memberId);
    }

    @Override
    public Optional<SseEmitter> get(String memberId) {
        return Optional.ofNullable(this.userEmitterMap.get(memberId));
    }
}
