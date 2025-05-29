package com.keremkulac.okeyscore.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

open class Migration2To3 : Migration(2, 3) {

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS finishedGame  (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "player1id INTEGER NOT NULL," +
                    "player1name TEXT NOT NULL," +
                    "player1allScores TEXT," +
                    "player1totalScore TEXT NOT NULL," +
                    "player1penalties TEXT," +
                    "player2id INTEGER NOT NULL," +
                    "player2name TEXT NOT NULL," +
                    "player2allScores TEXT," +
                    "player2totalScore TEXT NOT NULL," +
                    "player2penalties TEXT," +
                    "player3id INTEGER NOT NULL," +
                    "player3name TEXT NOT NULL," +
                    "player3allScores TEXT," +
                    "player3totalScore TEXT NOT NULL," +
                    "player3penalties TEXT," +
                    "player4id INTEGER NOT NULL," +
                    "player4name TEXT NOT NULL," +
                    "player4allScores TEXT," +
                    "player4totalScore TEXT NOT NULL," +
                    "player4penalties TEXT," +
                    "gameInfogameInfo TEXT NOT NULL," +
                    "gameInfodate TEXT NOT NULL " +
                    ")"
        )
        db.execSQL(
            """
                INSERT INTO finishedGame (
                    player1id, player1name, player1allScores, player1totalScore, player1penalties,
                    player2id, player2name, player2allScores, player2totalScore, player2penalties,
                    player3id, player3name, player3allScores, player3totalScore, player3penalties,
                    player4id, player4name, player4allScores, player4totalScore, player4penalties,
                    gameType, gameInfogameInfo, gameInfodate
                )
                SELECT
                    team1id, team1name, team1allScores, team1totalScore, team1penalties,
                    team2id, team2name, team2allScores, team2totalScore, team2penalties,
                    0, '', NULL, '', NULL,
                    0, '', NULL, '', NULL,
                    'PARTNER' as gameType,
                    gameInfogameInfo, gameInfodate
                FROM finishedPartnerGame
            """.trimIndent()
        )

        db.execSQL(
            """
                INSERT INTO finishedGame (
                    player1id, player1name, player1allScores, player1totalScore, player1penalties,
                    player2id, player2name, player2allScores, player2totalScore, player2penalties,
                    player3id, player3name, player3allScores, player3totalScore, player3penalties,
                    player4id, player4name, player4allScores, player4totalScore, player4penalties,
                    gameType, gameInfogameInfo, gameInfodate
                )
                SELECT 
                    player1id, player1name, player1allScores, player1totalScore, player1penalties,
                    player2id, player2name, player2allScores, player2totalScore, player2penalties,
                    player3id, player3name, player3allScores, player3totalScore, player3penalties,
                    player4id, player4name, player4allScores, player4totalScore, player4penalties,
                    'single',gameInfogameInfo, gameInfodate
                FROM finishedSingleGame
            """.trimIndent()
        )

    }
}