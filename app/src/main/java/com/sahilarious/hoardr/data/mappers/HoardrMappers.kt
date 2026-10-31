package com.sahilarious.hoardr.data.mappers

import com.sahilarious.hoardr.data.db.LinkEntity
import com.sahilarious.hoardr.domain.model.LinkModel

fun LinkEntity.toDomain(): LinkModel {
    return LinkModel(
        id = id,
        url = url,
        title = title,
        description = description,
        imageUrl = imageUrl,
        category = category,
        timestamp = timestamp,
        isRead = isRead,
        status = status,
        isPriority = isPriority,
    )
}

fun LinkModel.toEntity(): LinkEntity {
    return LinkEntity(
        id = id,
        url = url,
        title = title,
        description = description,
        imageUrl = imageUrl,
        category = category,
        timestamp = timestamp,
        isRead = isRead,
        status = status,
        isPriority = isPriority,
    )
}
