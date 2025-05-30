package com.keremkulac.okeyscore.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

open class Migration2To3 : Migration(2, 3) {

    override fun migrate(db: SupportSQLiteDatabase) {
        if (db.version < 3) {
            db.execSQL(
                """
                    CREATE TABLE IF NOT EXISTS finishedPartnerGame_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        team1Name TEXT NOT NULL,
                        team2Name TEXT NOT NULL,
                        team1TotalScore INTEGER NOT NULL,
                        team2TotalScore INTEGER NOT NULL,
                        team1Player1id INTEGER,
                        team1Player1name TEXT,
                        team1Player1allScores TEXT,
                        team1Player1totalScore TEXT,
                        team1Player1penalties TEXT,
                        team1Player2id INTEGER,
                        team1Player2name TEXT,
                        team1Player2allScores TEXT,
                        team1Player2totalScore TEXT,
                        team1Player2penalties TEXT,
                        team2Player1id INTEGER,
                        team2Player1name TEXT,
                        team2Player1allScores TEXT,
                        team2Player1totalScore TEXT,
                        team2Player1penalties TEXT,
                        team2Player2id INTEGER,
                        team2Player2name TEXT,
                        team2Player2allScores TEXT,
                        team2Player2totalScore TEXT,
                        team2Player2penalties TEXT,
                        gameInfogameInfo TEXT NOT NULL,
                        gameInfodate TEXT NOT NULL
                    )
                """
            )

            db.execSQL(
                """
                    INSERT INTO finishedPartnerGame_new (
                        id, team1Name, team2Name, team1TotalScore, team2TotalScore,
                        team1Player1id, team1Player1name, team1Player1allScores, 
                        team1Player1totalScore, team1Player1penalties,
                        team1Player2id, team1Player2name, team1Player2allScores,
                        team1Player2totalScore, team1Player2penalties,
                        team2Player1id, team2Player1name, team2Player1allScores,
                        team2Player1totalScore, team2Player1penalties,
                        team2Player2id, team2Player2name, team2Player2allScores,
                        team2Player2totalScore, team2Player2penalties,
                        gameInfogameInfo, gameInfodate
                    )
                    SELECT 
                        id,
                        team1Name as team1Name,
                        team2Name as team2Name,
                        0 as team1TotalScore,
                        0 as team2TotalScore,
                        team1id as team1Player1id,
                        team1name as team1Player1name,
                        team1allScores as team1Player1allScores,
                        team1totalScore as team1Player1totalScore,
                        team1penalties as team1Player1penalties,
                        NULL as team1Player2id,
                        NULL as team1Player2name,
                        NULL as team1Player2allScores,
                        NULL as team1Player2totalScore,
                        NULL as team1Player2penalties,
                        team2id as team2Player1id,
                        team2name as team2Player1name,
                        team2allScores as team2Player1allScores,
                        team2totalScore as team2Player1totalScore,
                        team2penalties as team2Player1penalties,
                        NULL as team2Player2id,
                        NULL as team2Player2name,
                        NULL as team2Player2allScores,
                        NULL as team2Player2totalScore,
                        NULL as team2Player2penalties,
                        gameInfogameInfo,
                        gameInfodate
                    FROM finishedPartnerGame
                """
            )

            db.execSQL("DROP TABLE finishedPartnerGame")
            db.execSQL("ALTER TABLE finishedPartnerGame_new RENAME TO finishedPartnerGame")
        }
    }
}