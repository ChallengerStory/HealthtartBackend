package com.dev5ops.healthtart.routine.exception;

public class RoutineNotFoundException extends IllegalArgumentException {
//    private final StatusEnum status;

    private static final String message = "해당 운동루틴이 존재하지 않습니다.";

    public RoutineNotFoundException() {
        super(message);
//        this.status = StatusEnum.ROUTINE_NOT_FOUND;
    }
}
