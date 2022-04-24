package com.korilin.domain.table

import org.ktorm.entity.Entity
import org.ktorm.schema.*

interface UserProfile: Entity<UserProfile> {

    companion object : Entity.Factory<UserProfile>()

    var id: Int
    var login: String
    var name: String
    var email: String
    var avatarUrl: String
    var bio: String
    var htmlUrl: String
    var block: Boolean
}

object UserProfiles: Table<UserProfile>("t_user_profile") {
    val id = int("id").primaryKey().bindTo { it.id }
    val login = varchar("login").bindTo { it.login }
    val name = varchar("name").bindTo { it.name }
    val email = varchar("email").bindTo { it.email }
    val avatarUrl = text("avatar_url").bindTo { it.avatarUrl }
    val bio = text("bio").bindTo { it.bio }
    val htmlUrl = text("html_url").bindTo { it.htmlUrl }
    val block = boolean("block").bindTo { it.block }
}
