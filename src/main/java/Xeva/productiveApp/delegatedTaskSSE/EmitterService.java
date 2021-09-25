package Xeva.productiveApp.delegatedTaskSSE;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class EmitterService {
    private final long eventsTimeout;
    private final EmitterRepository repository;

    public EmitterService(EmitterRepository repository) {
        this.eventsTimeout = -1L;
        this.repository = repository;
    }

    public SseEmitter createEmitter(String memberId) {
        SseEmitter emitter = new SseEmitter(this.eventsTimeout);
        emitter.onCompletion(() -> this.repository.remove(memberId));
        emitter.onTimeout(() -> this.repository.remove(memberId));
        emitter.onError(e -> this.repository.remove(memberId));
        this.repository.addOrReplaceEmitter(memberId, emitter);
        return emitter;
    }

    public SseEmitter createCollaboratorEmitter(String memberId) {
        SseEmitter emitter = new SseEmitter(this.eventsTimeout);
        emitter.onCompletion(() -> this.repository.remove(memberId));
        emitter.onTimeout(() -> this.repository.remove(memberId));
        emitter.onError(e -> this.repository.remove(memberId));
        this.repository.addOrReplaceEmitter(memberId + "collaborator", emitter);
        return emitter;
    }
}
