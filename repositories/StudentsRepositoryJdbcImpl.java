package com.company.repositories;

import com.company.task.Mentor;
import com.company.task.Student;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentsRepositoryJdbcImpl implements StudentsRepository {


    // language=SQL
    private static final String SQL_SELECT_BY_ID = "select * from student where id = ";
    // language=SQL
    private static final String SQL_INSERT_STUDENT = "insert into student(first_name, last_name, age, group_number) " +
            "values ('%s', '%s', %d, %d)";
    // language=SQL
    private static final String SQL_INSERT_MENTOR = "insert into mentor(first_name, last_name, subject_id, " +
            "student_id) values ('%s', '%s', %d, %d)";
    // language=SQL
    private static final String SQL_SELECT_SUBJECT_ID = "select id from subject where title = '%s'";
    // language=SQL
    private static final String SQL_INSERT_SUBJECT = "insert into subject(title) values ('%s')";
    // language=SQL
    private static final String SQL_UPDATE_STUDENT = "update student set " +
            "first_name = '%s', " +
            "last_name = '%s', " +
            "age = %d, " +
            "group_number = %d " +
            "where id = %d";
    // language=SQL
    private static final String SQL_MENTOR_DELETE_BY_STUDENT_ID = "delete from mentor where student_id = ";
    // language=SQL
    private static final String SQL_SELECT_FIND_ALL = "select " +
            "       m.id           as m_id,\n" +
            "       m.first_name   as m_name,\n" +
            "       m.last_name    as m_lname,\n" +
            "       s.id           as s_id,\n" +
            "       s.first_name   as s_name,\n" +
            "       s.last_name    as s_lname,\n" +
            "       s.age          as s_age,\n" +
            "       s.group_number as group_num\n" +
            "from mentor m\n" +
            "         full outer join student s on m.student_id = s.id";
    // language=SQL
    private  static final String SQL_SELECT_FIND_ALL_BY_AGE = "select " +
            "       m.id           as m_id,\n" +
            "       m.first_name   as m_name,\n" +
            "       m.last_name    as m_lname,\n" +
            "       s.id           as s_id,\n" +
            "       s.first_name   as s_name,\n" +
            "       s.last_name    as s_lname,\n" +
            "       s.age          as s_age,\n" +
            "       s.group_number as group_num\n" +
            "from mentor m\n" +
            "         full outer join student s on m.student_id = s.id " +
            "where s.age = ";


    Connection connection;


    public StudentsRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public List<Student> findAllByAge(int age) {

        Statement statement = null;
        ResultSet result = null;

        List<Student> students = new ArrayList<>();

        try {
            statement = connection.createStatement();
            result = statement.executeQuery(SQL_SELECT_FIND_ALL_BY_AGE + age);
            addStudents(result, students);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }

        return students;
    }

    private void addStudents(ResultSet resultSet, List<Student> students) {
        try {
            while (resultSet.next()) {
                long studentID = resultSet.getLong("s_id");
                Student student;
                if (getStudent(students, studentID) == null) {
                    // save new student;
                    student = new Student(
                            studentID,
                            resultSet.getString("s_name"),
                            resultSet.getString("s_lname"),
                            resultSet.getInt("s_age"),
                            resultSet.getInt("group_num")
                    );
                    students.add(student);
                }
                // add mentor
                resultSet.getLong("m_id");
                if (!resultSet.wasNull()) {
                    student = getStudent(students, studentID);
                    Mentor mentor = new Mentor(
                            resultSet.getLong("m_id"),
                            resultSet.getString("m_name"),
                            resultSet.getString("m_lname"),
                            null,
                            student);
                    List<Mentor> mentors;
                    if (student.getMentors() != null) {
                        mentors = student.getMentors();
                    } else {
                        mentors = new ArrayList<>();
                    }
                    mentors.add(mentor);
                    student.setMentors(mentors);
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
    }

    @Override
    public List<Student> findAll() {

        Statement statement = null;
        ResultSet result = null;

        List<Student> students = new ArrayList<>();

        try {
            statement = connection.createStatement();
            result = statement.executeQuery(SQL_SELECT_FIND_ALL);
            addStudents(result, students);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
        return students;
    }

    private Student getStudent(List<Student> students, long studentID) {
        for (Student student : students) {
            if (student.getId() == studentID) {
                return student;
            }
        }
        return null;
    }


    @Override
    public Student findById(Long id) {

        Statement statement = null;
        ResultSet result = null;

        try {
            statement = connection.createStatement();
            result = statement.executeQuery(SQL_SELECT_BY_ID + id);
            if (result.next()) {
                return new Student(
                        result.getLong("id"),
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getInt("age"),
                        result.getInt("group_number")
                );
            } else return null;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
    }


    @Override
    public void save(Student entity) {

        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(String.format(SQL_INSERT_STUDENT,
                    entity.getFirstName(),
                    entity.getLastName(),
                    entity.getAge(),
                    entity.getGroupNumber()), Statement.RETURN_GENERATED_KEYS);

            resultSet = statement.getGeneratedKeys();
            resultSet.next();
            long studentID = resultSet.getLong(1);
            if (entity.getMentors() != null) {
                addMentors(entity.getMentors(), studentID);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    //ignore
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
    }


    @Override
    public void update(Student entity) {
        Statement statement = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(String.format(SQL_UPDATE_STUDENT,
                    entity.getFirstName(),
                    entity.getLastName(),
                    entity.getAge(),
                    entity.getGroupNumber(),
                    entity.getId()));

            // delete old and set new mentors (Alpha-version)

            /* Было принято решение оставить так, просто удаляя всех старых менторов
             * добавляя новых. Однако данное решение не лучшее, так как любое изменение
             * повлечет за собой удаление и восстановление данных в таблице менторах,
             * что не является оптимальным решением.
             * Хорошее решение - проверять каждого ментора на наличие в таблице с student_id
             * равным id студента. Данное решение выйдет в Beta-верссии данной программы */

            statement.execute(SQL_MENTOR_DELETE_BY_STUDENT_ID + entity.getId());
            addMentors(entity.getMentors(), entity.getId());

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
    }


    private void addMentors(List<Mentor> mentors, long studentID) {

        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            for (Mentor mentor : mentors) {
                if (mentor != null) {
                    // getting subject id
                    statement = connection.createStatement();
                    resultSet = statement.executeQuery(String.format(SQL_SELECT_SUBJECT_ID,
                            mentor.getSubject()));
                    long subjectID;

                    if (resultSet.next()) {
                        // if the subject already in the table
                        subjectID = resultSet.getLong("id");
                    } else {
                        // if this new subject
                        statement.executeUpdate(String.format(SQL_INSERT_SUBJECT,
                                mentor.getSubject()), Statement.RETURN_GENERATED_KEYS);

                        resultSet = statement.getGeneratedKeys();
                        resultSet.next();
                        subjectID = resultSet.getLong(1);
                    }
                    // mentor inserting
                    statement.executeUpdate(String.format(SQL_INSERT_MENTOR,
                            mentor.getFirstName().trim(),
                            mentor.getLastName().trim(),
                            subjectID,
                            studentID));
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    //ignore
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
    }
}