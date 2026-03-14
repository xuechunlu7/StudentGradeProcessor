package processor;

import model.GradeRecord;

public interface GradeCalculator {
    double calculateFinalGrade(GradeRecord record);
}