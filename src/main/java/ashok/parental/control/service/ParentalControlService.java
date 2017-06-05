package ashok.parental.control.service;

import java.util.Comparator;
import java.util.List;

import ashok.movie.meta.data.exception.TechnicalFailureException;
import ashok.movie.meta.data.exception.TitleNotFoundException;
import ashok.movie.meta.data.service.MovieService;
import ashok.parental.control.error.ParentalControlError;

public class ParentalControlService {

	private final MovieService movieService;
	private final List<String> controlLevels;

	public ParentalControlService(MovieService movieService, List<String> controlLevels) {
		this.movieService = movieService;
		this.controlLevels = controlLevels;
	}

	public boolean isAbleToWatch(String customerParentalControlLevel, String movieId, ParentalControlError error) {
		try {
			final String movieParentalControlLevel = movieService.getParentalControlLevel(movieId);

			return matchesCustomerControlLevel(customerParentalControlLevel, movieParentalControlLevel);
		} catch (TitleNotFoundException | TechnicalFailureException e) {
			error.setErrorMessage(e.getMessage());
			return false;
		}
	}

	private boolean matchesCustomerControlLevel(String customerParentalControlLevel, String movieParentalControlLevel) {
		Comparator<String> controlLevelComparator = (controlLevel1, controlLevel2) -> controlLevels.indexOf(controlLevel1) - controlLevels.indexOf(controlLevel2);
		return controlLevelComparator.compare(movieParentalControlLevel, customerParentalControlLevel) <= 0;
	}
}
