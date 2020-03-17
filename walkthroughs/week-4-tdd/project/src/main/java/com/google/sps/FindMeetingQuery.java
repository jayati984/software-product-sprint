// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

    //arraylist to store the time ranges that the attendants are busy
    ArrayList<TimeRange> timesToBlock = new ArrayList<TimeRange>();
    //arraylist collection to return the time ranges that all the attendants of the meeting are free
    Collection<TimeRange> times = new ArrayList<TimeRange>();

    //collection of all the attendants required for the meeting
    Collection<String> attendeesOfMeeting = request.getAttendees();
    //the length of the meeting
    long timeLength = request.getDuration();

    //if the meeting duration request is longer than a day, then the meeting is not possible
    if(timeLength > TimeRange.END_OF_DAY)
    {
        return times;
    }

    //for all the attendants of each event, if any of the attendants also need to attend the meeting,
    //then block that time range as busy
    for(Event event : events)
    {
        Set<String> attendeesOfEvent = event.getAttendees();
        for(String personToAttendMeeting : attendeesOfMeeting)
        {
            //ignore people not required to attend the meeting
            if(attendeesOfEvent.contains(personToAttendMeeting))
            {
                TimeRange busy = TimeRange.fromStartEnd(event.getWhen().start(), event.getWhen().end(), false);
                if(!(timesToBlock.contains(busy)))
                {
                    timesToBlock.add(busy);
                }    
            }
        }
    }

    //sort the times ranges in ascending order by their start time
    Collections.sort(timesToBlock, TimeRange.ORDER_BY_START);

    //arraylist to store time ranges after merging overlapping time ranges and nested time ranges
    ArrayList<TimeRange> newTimes = new ArrayList<TimeRange>();

    if(timesToBlock.size() >= 2)
    {
        for(int i = 1; i < timesToBlock.size(); i++)
        {
            if(timesToBlock.get(i-1).overlaps(timesToBlock.get(i)))
            {
                //overlapping events
                if(timesToBlock.get(i-1).start() < timesToBlock.get(i).start() 
                    && timesToBlock.get(i-1).end() < timesToBlock.get(i).end()
                    && timesToBlock.get(i).start() < timesToBlock.get(i-1).end())
                {
                    TimeRange newRange = TimeRange.fromStartEnd(timesToBlock.get(i-1).start(), 
                                                                timesToBlock.get(i).end(), false);
                    newTimes.add(newRange);
                }
                //double booked people and nested events
                else if(timesToBlock.get(i-1).contains(timesToBlock.get(i)))
                {
                    TimeRange newRange = TimeRange.fromStartEnd(timesToBlock.get(i-1).start(),
                                                                timesToBlock.get(i-1).end(), false);
                    newTimes.add(newRange);
                }
            }
            else
            {
                TimeRange newRange = timesToBlock.get(i-1);
                newTimes.add(newRange);
                newRange = timesToBlock.get(i);
                newTimes.add(newRange);
            }
        }
    }

    Collections.sort(newTimes, TimeRange.ORDER_BY_START);

    int freeStartRange = 0, freeEndRange = 0, nextFreeStartRange = 0;

    //no conflicts
    if(timesToBlock.size() == 0)
    {
        TimeRange freeTime = TimeRange.WHOLE_DAY;
        times.add(freeTime);
    }
    //split the day into two options if only one time range is blocked as busy
    else if(timesToBlock.size() == 1)
    {
        TimeRange freeTime = TimeRange.fromStartEnd(TimeRange.START_OF_DAY, timesToBlock.get(0).start(), false);
        times.add(freeTime);
        freeTime = TimeRange.fromStartEnd(timesToBlock.get(0).end(), TimeRange.END_OF_DAY, true);
        times.add(freeTime);
    }
    //split the day into two options if only one time range after merging overlapping and nested time ranges
    else if(newTimes.size() == 1)
    {
        TimeRange freeTime = TimeRange.fromStartEnd(TimeRange.START_OF_DAY, newTimes.get(0).start(), false);
        times.add(freeTime);
        freeTime = TimeRange.fromStartEnd(newTimes.get(0).end(), TimeRange.END_OF_DAY, true);
        times.add(freeTime);
    }
    //ensure just enough room if attendants are busy at start and end of day
    else if(newTimes.get(0).start() == TimeRange.START_OF_DAY)
    {
        freeStartRange = newTimes.get(0).end();
        int i;
        for(i = 1; i < newTimes.size(); i++)
        {
            freeEndRange = newTimes.get(i).start();
            nextFreeStartRange = newTimes.get(i).end();
            TimeRange freeTime = TimeRange.fromStartEnd(freeStartRange, freeEndRange, false);
            times.add(freeTime);
            freeStartRange = nextFreeStartRange;
        }
    }
    else
    {
        int i;
        for(i = 0; i < newTimes.size(); i++)
        {
            freeEndRange = newTimes.get(i).start();
            nextFreeStartRange = newTimes.get(i).end();
            TimeRange freeTime = TimeRange.fromStartEnd(freeStartRange, freeEndRange, false);
            times.add(freeTime);
            freeStartRange = nextFreeStartRange;
        }
        freeEndRange = TimeRange.END_OF_DAY;
        TimeRange freeTime = TimeRange.fromStartEnd(freeStartRange, freeEndRange, true);
        times.add(freeTime); 
    }
    return times;
  }
}
