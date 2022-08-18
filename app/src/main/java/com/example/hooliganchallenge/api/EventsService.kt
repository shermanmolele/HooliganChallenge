package com.example.hooliganchallenge.api

import com.example.hooliganchallenge.models.EventResponse
import com.example.hooliganchallenge.models.Events
import com.example.hooliganchallenge.models.Schedule
import com.example.hooliganchallenge.models.ScheduleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EventsService {

    @GET("/getEvents")
    suspend fun getEvents(
    ): Response<EventResponse>
    @GET("/getSchedule")
    suspend fun getSchedule(): Response<ScheduleResponse>

    @GET("/getEvents")
    suspend fun getEvent(
        @Path("id")
        id : Int
    ): Response<EventResponse>

}
