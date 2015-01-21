package batch;

import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class AlbumItemWriter implements ItemWriter<Album> {

    private static final String INSERT_ALBUM = "insert into dest_table (id, album_data) values (?, ?)";

    private JdbcTemplate jdbcTemplate;

    public AlbumItemWriter(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void write(List<? extends Album> albums) throws Exception {
        for (Album a : albums){
            jdbcTemplate.update(INSERT_ALBUM,
                    a.getId(),
                    a.getAlbumData()
            );
        }
    }
}
