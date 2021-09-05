package com.java.qitianliang.SQLite;

import java.util.List;

public interface IEntityDB {
    List<Entity> getAllEntity();

    void deleteAllEntity();
    void deleteEntityByName(String Name);

    Entity getEntityByName(String Name);

    void insertAllEntity(List<Entity> entityList);

    void insertEntity(Entity entity);
}
