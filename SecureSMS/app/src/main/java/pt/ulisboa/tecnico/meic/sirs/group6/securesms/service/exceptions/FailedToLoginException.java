package pt.ulisboa.tecnico.meic.sirs.group6.securesms.service.exceptions;

/**
 * Created by lribeirogomes on 17/11/15.
 */
public class FailedToLoginException extends Exception {
    public FailedToLoginException(Throwable throwable){
        super("Failed to create password.", throwable);
    }
}