package com.github.mikesafonov.operatorbot.command;

import lombok.Getter;

@Getter
public enum Command {
    NONE(false, false, false, ""),

    START(true, true, true, "[/start](/start) - Показать стартовое сообщение."),
    HELP(true, true, true, "[/help](/help) - Помощь."),
    ROLE(true, true, true, "[/role](/role) - Узнать свою роль."),

    WHO(true, false, true, "[/who](/who) - Узнать кто дежурный сегодня."),
    WHEN_MY_DUTY(true, false, true, "[/when_my_duty](/when_my_duty) - Узнать когда я дежурный."),
    UPDATE_DUTY(true, false, false, "[/update_duty <<date>> <<telegramId>>](/update_duty) - Назначить дежурного."),

    ADD_USER(true, false, false, "[/add_user <<telegramId>> <<fullName>>](/add_user) - Добавить внешнего пользователя."),
    ADD_DUTY(true, false, false, "[/add_duty <<telegramId>> <<fullName>>](/add_duty) - Добавить внутреннего пользователя."),

    REASSIGN_DUTY(true, false, false, "[/reassign_duty](/reassign_duty) - Запустить процесс назначения дежурных."),
    TIMETABLE(true, false, false, "[/timetable <<days>>](/timetable) - Вывести расписание на указанное количество дней.");

    private final boolean admin;
    private final boolean external;
    private final boolean internal;
    private final String description;

    Command(boolean admin, boolean external, boolean internal, String description) {
        this.admin = admin;
        this.external = external;
        this.internal = internal;
        this.description = description;
    }
}
