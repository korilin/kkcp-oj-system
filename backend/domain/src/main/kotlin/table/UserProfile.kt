package com.korilin.table

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.text
import org.ktorm.schema.varchar

interface UserProfile: Entity<UserProfile> {

    companion object : Entity.Factory<UserProfile>()

    var id: Int
    var login: String
    var name: String
    var email: String
    var avatarUrl: String
    var bio: String
    var htmlUrl: String
}

object UserProfiles: Table<UserProfile>("user_profile") {
    val id = int("id").primaryKey().bindTo { it.id }
    val login = varchar("login").bindTo { it.login }
    val name = varchar("name").bindTo { it.name }
    val email = varchar("email").bindTo { it.email }
    val avatarUrl = text("avatar_url").bindTo { it.avatarUrl }
    val bio = text("bio").bindTo { it.bio }
    val htmlUrl = text("html_url").bindTo { it.htmlUrl }
}
