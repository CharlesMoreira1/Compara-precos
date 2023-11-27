package com.z1.comparaprecos.core.database.util

interface IBuildQuery {

    /**
     * Select the columns to return its values.
     * @param selectedFields
     * @return SELECT (selectedFields) FROM or SELECT * FROM if selectedFields is null
     */
    fun select(selectedFields: List<String>?): IBuildQuery

    /**
     * Select the database table.
     * @param tableName
     * @return table name
     */
    fun fromTable(tableName: String): IBuildQuery

    /**
     * Put the whereClause to build the query.
     * @param whereClause
     * @return WHERE (whereClause) AND (AND will be used if whereClause is > 1) or empty string if whereClause is null
     */
    fun where(whereClause: List<String>?): IBuildQuery

    /**
     * Order by data.
     * @param orderByField
     * @return ORDER BY orderByField or empty string if orderByField is null
     */
    fun orderBy(orderByField: String?): IBuildQuery

    /**
     * Limit data.
     * @param limitClause
     * @return LIMIT(limitClause) or empty string if limitClause is null
     */
    fun limit(limitClause: Int?): IBuildQuery

    /**
     * Build the query.
     * @return sql query
     */
    fun build(): String
}

class BuildQuery: IBuildQuery {
    private var select: String = ""
    private var fromTable: String = ""
    private var whereClause: String = ""
    private var orderBy: String = ""
    private var limit: String = ""
    override fun select(selectedFields: List<String>?): IBuildQuery {
        val select = selectedFields?.let {
            "SELECT${it.joinToString(", ")}FROM"
        } ?: "SELECT * FROM"

        this.select = select
        return this
    }

    override fun fromTable(tableName: String): IBuildQuery {
        this.fromTable = tableName
        return this
    }

    override fun where(whereClause: List<String>?): IBuildQuery {
        whereClause?.let {
            this.whereClause = "WHERE ${it.joinToString("AND ")}"
        }
        return this
    }

    override fun orderBy(orderByField: String?): IBuildQuery {
        orderByField?.let {
            this.orderBy = "ORDER BY $it"
        }
        return this
    }

    override fun limit(limitClause: Int?): IBuildQuery {
        limitClause?.let {
            this.limit = "LIMIT ($it)"
        }
        return this
    }

    override fun build() =
        "$select $fromTable $whereClause $orderBy $limit"
}