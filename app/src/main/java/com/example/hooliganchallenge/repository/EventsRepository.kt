package com.example.hooliganchallenge.repository

import com.example.hooliganchallenge.api.EventsService


class EventsRepository(private var api: EventsService) {
    suspend fun getEvents() = api.getEvents()
    suspend fun getSchedule() = api.getSchedule()
    suspend fun getEvent(id: Int) = api.getEvent(id = id)
}
