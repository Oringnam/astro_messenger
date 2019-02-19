package astro.grpc.server.dao;

import astro.grpc.server.domain.AstroMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;

public interface MariaDAO {
//
//    public int connectionTest(String table);

    public void createTable(@Param("table") String table);

    @Mapper
    public void insert(@Param("table") String table, @Param("astroMessage") AstroMessage astroMessage);
}
