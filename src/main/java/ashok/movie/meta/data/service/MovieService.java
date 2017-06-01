package ashok.movie.meta.data.service;

import ashok.movie.meta.data.exception.TechnicalFailureException;
import ashok.movie.meta.data.exception.TitleNotFoundException;

public interface MovieService {

	String getParentalControlLevel(String movieId) throws TitleNotFoundException, TechnicalFailureException;
}
