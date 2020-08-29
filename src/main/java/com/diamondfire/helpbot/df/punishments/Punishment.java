package com.diamondfire.helpbot.df.punishments;

import com.diamondfire.helpbot.util.*;

import java.util.Date;

public class Punishment {

    public final PunishmentType type;
    public final String uuid;
    public final String reason;
    public final String staffUUID;
    public final String staffName;
    public final Date startTime;
    public final Date untilTime;
    public final boolean silent;
    public final boolean active;
    public final String removeByUUID;
    public final String removeByName;
    public final Date removeDate;

    public Punishment(PunishmentType type, String uuid, String reason, String staffUUID, String staffName, Date startTime, Date untilTime, boolean silent, boolean active, String removeByUUID, String removeByName, Date removeDate) {
        this.type = type;
        this.uuid = uuid;
        this.reason = StringUtil.display(reason);
        this.staffUUID = staffUUID;
        this.staffName = staffName;
        this.startTime = startTime;
        this.untilTime = untilTime;
        this.silent = silent;
        this.active = active;
        this.removeByUUID = removeByUUID;
        this.removeByName = removeByName;
        this.removeDate = removeDate;
    }

    private static String formatUntil(Date date) {
        if (date == null) return "Never";

        return FormatUtil.formatDate(date);
    }

    @Override
    public String toString() {
        String given = "Given " + FormatUtil.formatDate(startTime);
        String reasonGiven = reason.isBlank() ? "No Reason Given" : reason;
        String expire = (active ? "Expires " : "Expired ") + formatUntil(untilTime);
        String duration = "";

        if (type == PunishmentType.KICK) {
            expire = "";
        }

        if (untilTime != null && (type == PunishmentType.BAN || type == PunishmentType.MUTE)) {
            duration = String.format("(Duration %s)", FormatUtil.formatMilliTime(untilTime.getTime() - startTime.getTime()));
        }

        return String.format("[%s] %s %s %s \n%s", type, given, expire, duration, reasonGiven);
    }
}

