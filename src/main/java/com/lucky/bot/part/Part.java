package com.lucky.bot.part;

import com.lucky.bot.part.grade.PartGrade;
import com.lucky.bot.part.type.BaseType;

public record Part(int id, String name, BaseType type, PartGrade grade) {
}
