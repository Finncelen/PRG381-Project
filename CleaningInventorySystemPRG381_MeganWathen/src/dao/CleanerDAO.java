package dao;

import java.util.ArrayList;
import java.util.List;
import models.Cleaner;
import utils.IDGenerator;

public class CleanerDAO {

    private static final List<Cleaner> cleaners = new ArrayList<>();

    public boolean addCleaner(Cleaner cleaner) {
        if (cleaner == null
                || employeeNumberExists(cleaner.getEmployeeNumber())) {
            return false;
        }

        cleaner.setCleanerId(IDGenerator.nextCleanerID());
        cleaners.add(cleaner);
        return true;
    }

    public boolean updateCleaner(Cleaner updatedCleaner) {
        for (int i = 0; i < cleaners.size(); i++) {
            if (cleaners.get(i).getCleanerId()
                    == updatedCleaner.getCleanerId()) {

                cleaners.set(i, updatedCleaner);
                return true;
            }
        }

        return false;
    }

    public boolean deleteCleaner(int cleanerId) {
        return cleaners.removeIf(
                cleaner -> cleaner.getCleanerId() == cleanerId
        );
    }

    public Cleaner getCleanerById(int cleanerId) {
        for (Cleaner cleaner : cleaners) {
            if (cleaner.getCleanerId() == cleanerId) {
                return cleaner;
            }
        }

        return null;
    }

    public List<Cleaner> getAllCleaners() {
        return new ArrayList<>(cleaners);
    }

    public List<Cleaner> searchCleaners(String searchText) {
        List<Cleaner> results = new ArrayList<>();

        if (searchText == null) {
            return results;
        }

        String search = searchText.trim().toLowerCase();

        for (Cleaner cleaner : cleaners) {
            if (cleaner.getFirstName().toLowerCase().contains(search)
                    || cleaner.getLastName().toLowerCase().contains(search)
                    || cleaner.getEmployeeNumber()
                            .toLowerCase().contains(search)
                    || cleaner.getDepartment()
                            .toLowerCase().contains(search)) {

                results.add(cleaner);
            }
        }

        return results;
    }

    public boolean employeeNumberExists(String employeeNumber) {
        if (employeeNumber == null) {
            return false;
        }

        for (Cleaner cleaner : cleaners) {
            if (cleaner.getEmployeeNumber()
                    .equalsIgnoreCase(employeeNumber.trim())) {
                return true;
            }
        }

        return false;
    }
}