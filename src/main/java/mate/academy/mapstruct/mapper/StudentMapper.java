package mate.academy.mapstruct.mapper;

import java.util.List;
import mate.academy.mapstruct.config.MapperConfig;
import mate.academy.mapstruct.dto.student.CreateStudentRequestDto;
import mate.academy.mapstruct.dto.student.StudentDto;
import mate.academy.mapstruct.dto.student.StudentWithoutSubjectsDto;
import mate.academy.mapstruct.model.Student;
import mate.academy.mapstruct.model.Subject;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = GroupMapper.class)
public interface StudentMapper {
    @Mapping(source = "group.id", target = "groupId")
    @Mapping(target = "subjectIds", ignore = true)
    StudentDto toDto(Student student);

    @Mapping(source = "group.id", target = "groupId")
    StudentWithoutSubjectsDto toStudentWithoutSubjectsDto(Student student);

    @Mapping(target = "group", source = "groupId", qualifiedByName = "groupById")
    @Mapping(target = "subjects", ignore = true)
    Student toModel(CreateStudentRequestDto requestDto);

    @AfterMapping
    default void setSubjects(CreateStudentRequestDto requestDto,
                             @MappingTarget Student student) {
        List<Subject> subjects = requestDto.subjects().stream()
                .map(Subject::new)
                .toList();
        student.setSubjects(subjects);
    }

    @AfterMapping
    default void setSubjectIds(@MappingTarget StudentDto studentDto, Student student) {
        List<Long> subjectIds = student.getSubjects().stream()
                .map(Subject::getId)
                .toList();
        studentDto.setSubjectIds(subjectIds);
    }
}
