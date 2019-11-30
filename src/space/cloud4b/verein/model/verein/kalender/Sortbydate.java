package space.cloud4b.verein.model.verein.kalender;

import java.time.Period;
import java.util.Comparator;

public class Sortbydate implements Comparator<Jubilaeum>
    {
        // Used for sorting in ascending order of
        // roll number
        public int compare(Jubilaeum a, Jubilaeum b)
        {
            return Period.between(b.getDatum(), a.getDatum()).getDays();
        }
    }

