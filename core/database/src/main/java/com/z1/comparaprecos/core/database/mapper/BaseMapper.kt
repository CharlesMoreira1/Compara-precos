package com.z1.comparaprecos.core.database.mapper

abstract class BaseMapper<Model, Entity> {
    abstract fun mapEntityToModel(entity: Entity): Model
    abstract fun mapModelToEntity(model: Model): Entity

    fun mapModelListToEntityList(modelList: List<Model>) = modelList.map { mapModelToEntity(it) }
    fun mapEntityListToModelList(entityList: List<Entity>) = entityList.map { mapEntityToModel(it) }
}