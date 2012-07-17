package net.ftlines.wicket.fullcalendar;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.util.time.Duration;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;

public class CalendarioConfig extends Config {
	private static final long serialVersionUID = 2215048621457491262L;

	private static final String[] monthNames = { "Janeiro", "Fevereiro", "Mar?o", "Abril", "Maio", "Junho", "Julho",
		"Agosto", "Setembro", "Outubro", "Novembro", "Dezembro" };
	private static final String[] monthNamesShort = { "Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set",
		"Out", "Nov", "Dez" };
	private static final String[] dayNames = { "Domingo", "Segunda", "Ter?a", "Quarta", "Quinta", "Sexta", "S?bado" };
	private static final String[] daynamesShort = { "Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "S?b" };

	public CalendarioConfig() {

		setSelectable(true);
		setSelectHelper(false);

		EventSource reservations = new EventSource();
		reservations.setTitle("Reservations");
		reservations.setEventsProvider(new RandomEventsProvider("Reservation "));
		reservations.setEditable(true);
		reservations.setBackgroundColor("#63BA68");
		reservations.setBorderColor("#63BA68");
		add(reservations);

		EventSource downtimes = new EventSource();
		downtimes.setTitle("Maintenance");
		downtimes.setBackgroundColor("#B1ADAC");
		downtimes.setBorderColor("#B1ADAC");
		downtimes.setEventsProvider(new RandomEventsProvider("Maintenance "));
		add(downtimes);

		EventSource other = new EventSource();
		other.setTitle("Other Reservations");
		other.setBackgroundColor("#E6CC7F");
		other.setBorderColor("#E6CC7F");
		other.setEventsProvider(new RandomEventsProvider("Other Reservations "));
		add(other);

		getHeader().setLeft("prev,next today");
		getHeader().setCenter("title");
		getHeader().setRight("month,agendaWeek,agendaDay");

		getButtonText().setToday("Hoje");
		getButtonText().setDay("Dia");
		getButtonText().setWeek("Semana");
		getButtonText().setMonth("Mês");

		setLoading("function(bool) { if (bool) $(\"#loading\").show(); else $(\"#loading\").hide(); }");

		setMinTime(new LocalTime(6, 30));
		setMaxTime(new LocalTime(17, 30));
		setAllDaySlot(false);

		setColumnFormatDay("dddd, dd / MM / yyyy");
		setTitleFormatDay("dddd, dd / MM / yyyy");
		setTitleFormatWeek("d[ yyyy]{ '&#8212;'[ MMM] d MMMM/yyyy}");
		setTitleFormatMonth("MMMM/yyyy");

		setMonthNames(Arrays.asList(monthNames));
		setMonthNamesShort(Arrays.asList(monthNamesShort));
		setDayNames(Arrays.asList(dayNames));
		setDayNamesShort(Arrays.asList(daynamesShort));

		setFirstDay(1);
		setAxisFormat("H:mm");
		setTimeFormat("H:mm{ - H:mm}");

		setEditable(true);

	}

	private static class RandomEventsProvider implements EventProvider {
		private static final long serialVersionUID = 8366949426273497376L;

		Map<Integer, Event> events = new HashMap<Integer, Event>();

		private final String title;

		public RandomEventsProvider(String title) {
			this.title = title;
		}

		@Override
		public Collection<Event> getEvents(DateTime start, DateTime end) {
			events.clear();
			SecureRandom random = new SecureRandom();

			Duration duration = Duration.valueOf(end.getMillis() - start.getMillis());

			for (int j = 0; j < 1; j++) {
				for (int i = 0; i < duration.days() + 1; i++) {
					DateTime calendar = start;
					calendar = calendar.plusDays(i).withHourOfDay(6 + random.nextInt(10));

					Event event = new Event();
					int id = (int) (j * duration.days() + i) + random.nextInt(99999);
					event.setId("" + id);
					event.setTitle(title + (1 + i));
					event.setStart(calendar);
					calendar = calendar.plusHours(random.nextInt(8));
					event.setEnd(calendar);

					events.put(id, event);
				}
			}
			return events.values();
		}

		@Override
		public Event getEventForId(String id) throws EventNotFoundException {
			Integer idd = Integer.valueOf(id);
			Event event = events.get(idd);
			if (event != null) {
				return event;
			}
			throw new EventNotFoundException("Event with id: " + id + " not found");
		}

	}
}
