package astro.grpc.server.dao;

import astro.grpc.server.domain.AstroMessage;

public interface MariaDAO {

//    public void createDB(String dataBase);

//    public void createTable(String table);

    public void insert(AstroMessage astroMessage);
}
