package ashok.parental.control.service;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import ashok.movie.meta.data.exception.TechnicalFailureException;
import ashok.movie.meta.data.exception.TitleNotFoundException;
import ashok.movie.meta.data.service.MovieService;
import ashok.parental.control.error.ParentalControlError;

@RunWith(MockitoJUnitRunner.class)
public class ParentalControlServiceTest {

	@InjectMocks
	private ParentalControlService SUT; // Source Under Test

	@Mock
	private MovieService movieService;

	@Mock
	private ParentalControlError error;

	private List<String> controlLevels = Arrays.asList("U", "PG", "12", "15", "18");

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		SUT = new ParentalControlService(movieService, controlLevels);
	}

	@Test
	public void shouldReturnErrorMessageWhenMovieIdNotAvailable() throws Exception {
		// Given
		String expectedErrorMessage = "The movie service could not find the given movie";
		String movieId = "movieId1";
		String customerParentalControlLevel = "PG";
		when(movieService.getParentalControlLevel(movieId)).thenThrow(new TitleNotFoundException(expectedErrorMessage));

		// When
		final boolean actualResult = SUT.isAbleToWatch(customerParentalControlLevel, movieId, error);

		// Then
		assertThat(actualResult, is(false));
		verify(error, times(1)).setErrorMessage(expectedErrorMessage);
		verify(error).setErrorMessage(eq(expectedErrorMessage));
	}

	@Test
	public void shouldReturnErrorMessageWhenTechnicallyFailured() throws Exception {
		// Given
		String expectedErrorMessage = "System error";
		String movieId = "movieId1";
		String customerParentalControlLevel = "PG";
		when(movieService.getParentalControlLevel(movieId))
				.thenThrow(new TechnicalFailureException(expectedErrorMessage));

		// When
		final boolean actualResult = SUT.isAbleToWatch(customerParentalControlLevel, movieId, error);

		// Then
		assertThat(actualResult, is(false));
		verify(error, times(1)).setErrorMessage(expectedErrorMessage);
		verify(error).setErrorMessage(eq(expectedErrorMessage));
	}

	@Test
	public void shouldBeAbleToWatchWhenMovieLevelLessThanCustomerLevel() throws Exception {
		// Given
		String customerParentalControlLevel = "PG";
		String movieId = "movieId1";

		when(movieService.getParentalControlLevel(movieId)).thenReturn("U");

		// When
		final boolean actualResult = SUT.isAbleToWatch(customerParentalControlLevel, movieId, error);

		// Then
		assertThat(actualResult, is(true));
		verifyZeroInteractions(error);
		verify(error, times(0)).setErrorMessage(anyString());
	}

	@Test
	public void shouldBeAbleToWatchWhenMovieLevelEqualToCustomerLevel() throws Exception {
		// Given
		String customerParentalControlLevel = "12";
		String movieId = "movieId1";

		when(movieService.getParentalControlLevel(movieId)).thenReturn("12");

		// When
		final boolean actualResult = SUT.isAbleToWatch(customerParentalControlLevel, movieId, error);

		// Then
		assertThat(actualResult, is(true));
		verifyZeroInteractions(error);
		verify(error, times(0)).setErrorMessage(anyString());
	}

	@Test
	public void shouldNotBeAbleToWatchWhenMovieLevelHigherThanCustomerLevel() throws Exception {
		// Given
		String customerParentalControlLevel = "12";
		String movieId = "movieId1";

		when(movieService.getParentalControlLevel(movieId)).thenReturn("15");

		// When
		final boolean actualResult = SUT.isAbleToWatch(customerParentalControlLevel, movieId, error);

		// Then
		assertThat(actualResult, is(false));
		verifyZeroInteractions(error);
		verify(error, times(0)).setErrorMessage(anyString());
	}
}
