package br.com.ibout.service.datasource;

import static java.lang.Class.forName;
import static java.sql.DriverManager.getConnection;
import static org.apache.commons.lang3.StringUtils.trimToNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.ibout.model.local.Product;
import br.com.ibout.model.remote.Mapping;
import br.com.ibout.model.remote.Parameter;

@Service
public class DbServiceImpl implements DataSourceService {

	@Override
	public List<Product> read(Parameter parameter, Mapping mapping) {
		List<Product> listProduct = new ArrayList<Product>();
		Connection conn = null;
		Statement stmt = null;
		
		try {
			forName(parameter.getBdDriver());
			conn = getConnection(parameter.getBdUrl(), parameter.getBdUser(), parameter.getBdPass());
			stmt = conn.createStatement();
		      ResultSet rs = stmt.executeQuery(parameter.getBdSql());
		      
		      while(rs.next()){
		    	  listProduct.add(map(mapping, rs));
		       }
		      rs.close();
		      stmt.close();
		      conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listProduct;

	}
	
	private Product map(Mapping mapping, ResultSet rs) throws SQLException {
		return Product.builder()
				.idClient(mapping.getIdClient())
				.code(rs.getString(mapping.getCode() + 1) != null ? trimToNull(rs.getString(mapping.getCode() + 1)) : null)
				.brand(rs.getString(mapping.getBrand() + 1) != null ? trimToNull(rs.getString(mapping.getBrand() + 1)) : null)
				.complement(rs.getString(mapping.getComplement() + 1) != null ? trimToNull(rs.getString(mapping.getComplement() + 1)) : null)
				.description(rs.getString(mapping.getDescription() + 1) != null ? trimToNull(rs.getString(mapping.getDescription() + 1)) : null)
				.price(rs.getString(mapping.getPrice() + 1) != null ? trimToNull(rs.getString(mapping.getPrice() + 1)) : null)
				.build();
	}
	

}
