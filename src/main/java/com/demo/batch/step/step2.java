package com.demo.batch.step;

import com.demo.batch.processor.StudentPreProcessor;
import com.demo.entity.Student;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class step2 {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Autowired
    private DataSource dataSource;

    public step2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
    }

    @Bean
    public JdbcCursorItemReader<Student> readerStep2() {
        JdbcCursorItemReader<Student> itemReader = new JdbcCursorItemReader<>();
        itemReader.setDataSource(dataSource);
        itemReader.setSql("SELECT id, firstname, lastname, age FROM student");
        itemReader.setRowMapper(studentRowMapper());
        itemReader.setSaveState(true);
        return itemReader;
    }

    @Bean
    public RowMapper<Student> studentRowMapper() {
        return (rs, rowNum) -> {
            // Implement your row mapping logic here
            // You can also log the rowNum to check cursor position
            System.out.println("Cursor position: " + rowNum);

            Student student = new Student();
            student.setId((int) rs.getLong("id"));
            student.setFirstname(rs.getString("firstname"));
            student.setLastname(rs.getString("lastname"));
            student.setAge(rs.getInt("age"));
            return student;
        };
    }

    @Bean
    public StudentPreProcessor preProcessor() {
        return new StudentPreProcessor();
    }

    @Bean
    public FlatFileItemWriter<Student> writerStep2() {
        FlatFileItemWriter<Student> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("src/main/resources/students_processed.csv")); // Define the output file path
        writer.setLineAggregator(new DelimitedLineAggregator<Student>() {{
            setDelimiter(",");
            setFieldExtractor(new BeanWrapperFieldExtractor<Student>() {{
                setNames(new String[] {"id",  "firstname", "lastname","age"}); // Define the properties to be written
            }});
        }});
        return writer;
    }

    @Bean
    public Step stepCsvRead() {
        return new StepBuilder("csvRead", jobRepository)
                .<Student, Student>chunk(1000, platformTransactionManager)
                .reader(readerStep2())
                .processor(preProcessor())
                .writer(writerStep2())
                .build();
    }
}
