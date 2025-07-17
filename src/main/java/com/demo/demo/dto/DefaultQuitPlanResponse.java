package com.demo.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultQuitPlanResponse {
    private int id;
    private String startDate;
    private String targetDate;
    private int progress;
    private long cigarretesPerDay;
    private String quitReason;
    private long moneyTarget;
    private String note;

    public DefaultQuitPlanResponse(int id, String startDate, String targetDate, int progress, long cigarretesPerDay, String quitReason, long moneyTarget, String note) {
        this.id = id;
        this.startDate = startDate;
        this.targetDate = targetDate;
        this.progress = progress;
        this.cigarretesPerDay = cigarretesPerDay;
        this.quitReason = quitReason;
        this.moneyTarget = moneyTarget;
        this.note = note;
    }
}
