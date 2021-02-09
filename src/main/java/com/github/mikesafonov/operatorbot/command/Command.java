package com.github.mikesafonov.operatorbot.command;

import lombok.Getter;

@Getter
public enum Command {
    NONE(false,  false, ""),

    START(true,  true, "[/start](/start) - Показать стартовое сообщение."),
    HELP(true, true, "[/help](/help) - Помощь."),
    ROLE(true,  true, "[/role](/role) - Узнать свою роль."),
    WHO(true,  true, "[/who](/who) - Узнать кто дежурный сегодня."),
    WHEN_MY_DUTY(true,  true, "[/when_my_duty](/when_my_duty) - Узнать когда я дежурный."),

    UPDATE_DUTY(true,  false, "[/update_duty <<date>> <<telegramId>>](/update_duty) - Назначить дежурного."),
    ADD_USER(true,  false, "[/add_user <<telegramId>> <<fullName>>](/add_user) - Добавить нового пользователя."),
    REASSIGN_DUTY(true,  false, "[/reassign_duty](/reassign_duty) - Запустить процесс назначения дежурных."),
    TIMETABLE(true,  false, "[/timetable <<days>>](/timetable) - Вывести расписание на указанное количество дней.");

    private final boolean admin;
    private final boolean internal;
    private final String description;

    Command(boolean admin, boolean internal, String description) {
        this.admin = admin;
        this.internal = internal;
        this.description = description;
    }
}
