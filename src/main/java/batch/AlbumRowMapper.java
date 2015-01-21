package batch;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class AlbumRowMapper implements RowMapper<Album> {

    @Override
    public Album mapRow(ResultSet resultSet, int i) throws SQLException {
        Album album = new Album();
        album.setId(resultSet.getLong("id"));
        album.setAlbumData(Utils.clobToString(resultSet.getClob("album_data")));
        return album;
    }


}
