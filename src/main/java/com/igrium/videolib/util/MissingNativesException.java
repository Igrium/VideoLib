package com.igrium.videolib.util;

import net.minecraft.text.Text;

/**
 * Thrown when the native dependencies required for video playback are not found.
 */
public class MissingNativesException extends Exception {
    public static record HelpButton(Text text, String url) {}

    private Text errorText = Text.of("Unable to load video playback natives.");
    private HelpButton[] helpButtons = new HelpButton[0];

    public Text getErrorText() {
        return errorText;
    }
    
    public HelpButton[] getHelpButtons() {
        return helpButtons;
    }

    public MissingNativesException() {
        super("Unable to load video playback natives.");
    }

    public MissingNativesException(String message) {
        super(message);
        errorText = Text.literal(message);
    }

    public MissingNativesException(Throwable cause) {
        super(cause);
    }

    public MissingNativesException(String message, Throwable cause) {
        super(message, cause);
        errorText = Text.literal(message);
    }

    public MissingNativesException(Text errorText) {
        super(errorText.getString());
        this.errorText = errorText;
    }

    public MissingNativesException(Text errorText, String message) {
        super(message);
        this.errorText = errorText;
    }

    public MissingNativesException(Text errorText, Throwable cause) {
        super(errorText.getString(), cause);
        this.errorText = errorText;
    }
    
    public MissingNativesException(Text errorText, String message, Throwable cause) {
        super(message, cause);
    }

    public MissingNativesException(Text errorText, HelpButton[] helpButtons) {
        super(errorText.getString());
        this.helpButtons = helpButtons;
    }
    
    public MissingNativesException(Text errorText, String message, HelpButton[] helpButtons) {
        super(message);
        this.errorText = errorText;
        this.helpButtons = helpButtons;
    }

    public MissingNativesException(Text errorText, String message, HelpButton[] helpButtons, Throwable cause) {
        super(message, cause);
        this.errorText = errorText;
        this.helpButtons = helpButtons;
    }
}
