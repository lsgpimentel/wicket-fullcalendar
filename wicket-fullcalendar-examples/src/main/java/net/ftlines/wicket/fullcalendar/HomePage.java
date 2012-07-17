/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package net.ftlines.wicket.fullcalendar;

import net.ftlines.wicket.fullcalendar.callback.ClickedEvent;
import net.ftlines.wicket.fullcalendar.callback.DroppedEvent;
import net.ftlines.wicket.fullcalendar.callback.ResizedEvent;
import net.ftlines.wicket.fullcalendar.callback.SelectedRange;
import net.ftlines.wicket.fullcalendar.callback.View;
import net.ftlines.wicket.fullcalendar.selector.EventSourceSelector;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

public class HomePage extends WebPage {
	private static final long serialVersionUID = -3646576837399674643L;

	public HomePage() {

		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);

		FullCalendar calendar = new FullCalendar("cal", new CalendarioConfig()) {
			private static final long serialVersionUID = -6734717680095508365L;

			@Override
			protected void onDateRangeSelected(SelectedRange range, CalendarResponse response) {
				info("Selected region: " + range.getStart() + " - " + range.getEnd() + " / allDay: " + range.isAllDay());
				response.getTarget().add(feedback);
			}

			@Override
			protected boolean onEventDropped(DroppedEvent event, CalendarResponse response) {
				info("Event drop. eventId: " + event.getEvent().getId() + " sourceId: " + event.getSource().getUuid()
					+ " dayDelta: " + event.getDaysDelta() + " minuteDelta: " + event.getMinutesDelta() + " allDay: "
					+ event.isAllDay());
				info("Original start time: " + event.getEvent().getStart() + ", original end time: "
					+ event.getEvent().getEnd());
				info("New start time: " + event.getNewStartTime() + ", new end time: " + event.getNewEndTime());
				response.getTarget().add(feedback);
				return true;
			}

			@Override
			protected boolean onEventResized(ResizedEvent event, CalendarResponse response) {
				info("Event resized. eventId: " + event.getEvent().getId() + " sourceId: "
					+ event.getSource().getUuid() + " dayDelta: " + event.getDaysDelta() + " minuteDelta: "
					+ event.getMinutesDelta());
				response.getTarget().add(feedback);
				return true;
			}

			@Override
			protected void onEventClicked(ClickedEvent event, CalendarResponse response) {
				info("Event clicked. eventId: " + event.getEvent().getId() + ", sourceId: "
					+ event.getSource().getUuid());
				// response.refetchEvents();
				// response.removeAllEvents();
				// response.changeView(ViewType.AGENDA_WEEK);
				// response.refetchEvents(event.getSource());
				// response.select(new Date(), new Date("Sun, 22 Jul 2012 13:30:00 GMT"), false);
				response.getTarget().add(feedback);
			}

			@Override
			protected void onViewDisplayed(View view, CalendarResponse response) {
				info("View displayed. viewType: " + view.getType().name() + ", start: " + view.getStart() + ", end: "
					+ view.getEnd());
				response.getTarget().add(feedback);
			}
		};
		calendar.setMarkupId("calendar");
		add(calendar);
		add(new EventSourceSelector("selector", calendar));
	}

}
