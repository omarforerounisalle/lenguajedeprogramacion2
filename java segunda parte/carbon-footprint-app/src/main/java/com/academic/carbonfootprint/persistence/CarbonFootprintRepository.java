package com.academic.carbonfootprint.persistence;

import com.academic.carbonfootprint.contract.CarbonFootprint;

import java.io.IOException;
import java.util.List;

/**
 * Puerto de persistencia (DIP): la aplicación depende de la abstracción, no del CSV concreto.
 */
public interface CarbonFootprintRepository {

    void saveAll(List<CarbonFootprint> items) throws IOException;

    List<CarbonFootprint> loadAll() throws IOException;
}
