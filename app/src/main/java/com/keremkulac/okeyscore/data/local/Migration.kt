package com.keremkulac.okeyscore.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration : Migration(1, 2) {

    override fun migrate(db: SupportSQLiteDatabase) {
        if(db.version <2){
            db.execSQL("CREATE TABLE temp_finishedPartnerGame (" +
                    "id INTEGER PRIMARY KEY NOT NULL, " +
                    "team1id INTEGER, " +
                    "team1name TEXT, " +
                    "team1allScores TEXT, " +
                    "team1totalScore TEXT, " +
                    "team2id INTEGER, " +
                    "team2name TEXT, " +
                    "team2allScores TEXT, " +
                    "team2totalScore TEXT, " +
                    "gameInfogameInfo TEXT NOT NULL, " +
                    "gameInfodate TEXT NOT NULL " +
                    ")")

            db.execSQL("INSERT INTO temp_finishedPartnerGame SELECT * FROM finishedPartnerGame")
            db.execSQL("DROP TABLE finishedPartnerGame")
            db.execSQL("CREATE TABLE finishedPartnerGame (" +
                    "id INTEGER PRIMARY KEY NOT NULL, " +
                    "team1id INTEGER, " +
                    "team1name TEXT, " +
                    "team1allScores TEXT, " +
                    "team1totalScore TEXT, " +
                    "team2id INTEGER, " +
                    "team2name TEXT, " +
                    "team2allScores TEXT, " +
                    "team2totalScore TEXT, " +
                    "gameInfogameInfo TEXT NOT NULL, " +
                    "gameInfodate TEXT NOT NULL, " +
                    "team1penalties TEXT, " +
                    "team2penalties TEXT " +
                    ")")
            db.execSQL("ALTER TABLE temp_finishedPartnerGame ADD COLUMN team1penalties TEXT DEFAULT '[]'")
            db.execSQL("ALTER TABLE temp_finishedPartnerGame ADD COLUMN team2penalties TEXT DEFAULT '[]'")
            db.execSQL("INSERT INTO finishedPartnerGame SELECT * FROM temp_finishedPartnerGame")
            db.execSQL("DROP TABLE temp_finishedPartnerGame")


            db.execSQL("CREATE TABLE temp_finishedSingleGame (" +
                    "id INTEGER PRIMARY KEY NOT NULL, " +
                    "player1id INTEGER, " +
                    "player1name TEXT, " +
                    "player1allScores TEXT, " +
                    "player1totalScore TEXT, " +
                    "player2id INTEGER, " +
                    "player2name TEXT, " +
                    "player2allScores TEXT, " +
                    "player2totalScore TEXT, " +
                    "player3id INTEGER, " +
                    "player3name TEXT, " +
                    "player3allScores TEXT, " +
                    "player3totalScore TEXT, " +
                    "player4id INTEGER, " +
                    "player4name TEXT, " +
                    "player4allScores TEXT, " +
                    "player4totalScore TEXT, " +
                    "gameInfogameInfo TEXT NOT NULL, " +
                    "gameInfodate TEXT NOT NULL " +
                    ")")
            db.execSQL("INSERT INTO temp_finishedSingleGame SELECT * FROM finishedSingleGame")
            db.execSQL("DROP TABLE finishedSingleGame")
            db.execSQL("CREATE TABLE finishedSingleGame (" +
                    "id INTEGER PRIMARY KEY NOT NULL, " +
                    "player1id INTEGER, " +
                    "player1name TEXT, " +
                    "player1allScores TEXT, " +
                    "player1totalScore TEXT, " +
                    "player2id INTEGER, " +
                    "player2name TEXT, " +
                    "player2allScores TEXT, " +
                    "player2totalScore TEXT, " +
                    "player3id INTEGER, " +
                    "player3name TEXT, " +
                    "player3allScores TEXT, " +
                    "player3totalScore TEXT, " +
                    "player4id INTEGER, " +
                    "player4name TEXT, " +
                    "player4allScores TEXT, " +
                    "player4totalScore TEXT, " +
                    "gameInfogameInfo TEXT NOT NULL, " +
                    "gameInfodate TEXT NOT NULL, " +
                    "player1penalties TEXT, " +
                    "player2penalties TEXT, " +
                    "player3penalties TEXT, " +
                    "player4penalties TEXT " +
                    ")")
            db.execSQL("ALTER TABLE temp_finishedSingleGame ADD COLUMN player1penalties TEXT DEFAULT '[]'")
            db.execSQL("ALTER TABLE temp_finishedSingleGame ADD COLUMN player2penalties TEXT DEFAULT '[]'")
            db.execSQL("ALTER TABLE temp_finishedSingleGame ADD COLUMN player3penalties TEXT DEFAULT '[]'")
            db.execSQL("ALTER TABLE temp_finishedSingleGame ADD COLUMN player4penalties TEXT DEFAULT '[]'")
            db.execSQL("INSERT INTO finishedSingleGame SELECT * FROM temp_finishedSingleGame")
            db.execSQL("DROP TABLE temp_finishedSingleGame")
        }
    }
}