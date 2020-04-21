package ru.romanov.durak.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.romanov.durak.util.MessageHelper;

import static ru.romanov.durak.util.PageConstants.STUB_PAGE;
import static ru.romanov.durak.util.PageConstants.TITLE_ATTRIBUTE;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {

    private final MessageHelper messageHelper;

    @ExceptionHandler(Exception.class)
    public String mainHandler(Exception exception, Model model) {
        log.error("Main exception handler, class={} message={}", exception.getClass().getSimpleName(), exception.getMessage());
        model.addAttribute(TITLE_ATTRIBUTE, messageHelper.getMessage("stub.title"));
        return STUB_PAGE;
    }

}
