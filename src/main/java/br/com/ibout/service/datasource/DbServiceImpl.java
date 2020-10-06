package br.com.ibout.service.datasource;

import static java.lang.Class.forName;
import static java.sql.DriverManager.getConnection;
import static org.apache.commons.lang3.StringUtils.isBlank;
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
		ResultSet rs = null;
				
		try {
			forName(parameter.getBdDriver());
			conn = getConnection(parameter.getBdUrl(), parameter.getBdUser(), parameter.getBdPass());
			stmt = conn.createStatement();
			if(!isBlank(parameter.getBdSqlConnDbLink())) {
				rs = stmt.executeQuery(parameter.getBdSqlConnDbLink());
			}
			rs = stmt.executeQuery(parameter.getBdSql());
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
				.code(mapping.getCode() != null && rs.getString(mapping.getCode() + 1) != null ? trimToNull(rs.getString(mapping.getCode() + 1)) : null)
				.description(mapping.getDescription() != null && rs.getString(mapping.getDescription() + 1) != null ? trimToNull(rs.getString(mapping.getDescription() + 1)) : null)
				.brand(mapping.getBrand() != null && rs.getString(mapping.getBrand() + 1) != null ? trimToNull(rs.getString(mapping.getBrand() + 1)) : null)
				.complement(mapping.getComplement() != null && rs.getString(mapping.getComplement() + 1) != null ? trimToNull(rs.getString(mapping.getComplement() + 1)) : null)
				.groupProduct(mapping.getGroupProduct() != null && rs.getString(mapping.getGroupProduct() + 1) != null ? trimToNull(rs.getString(mapping.getGroupProduct() + 1)) : null)
				.price(mapping.getPrice() != null && rs.getString(mapping.getPrice() + 1) != null ? trimToNull(rs.getString(mapping.getPrice() + 1)) : null)
				.priceSold(mapping.getPriceSold() != null && rs.getString(mapping.getPriceSold() + 1) != null ? trimToNull(rs.getString(mapping.getPriceSold() + 1)) : null)
				.priceClub(mapping.getPriceClub() != null && rs.getString(mapping.getPriceClub() + 1) != null ? trimToNull(rs.getString(mapping.getPriceClub() + 1)) : null)
				.sold(mapping.getSold() != null && rs.getString(mapping.getSold() + 1) != null ? trimToNull(rs.getString(mapping.getSold() + 1)) : null)
				.stock(mapping.getStock() != null && rs.getString(mapping.getStock() + 1) != null ? trimToNull(rs.getString(mapping.getStock() + 1)) : null)
				.internalCode(mapping.getInternalCode() != null && rs.getString(mapping.getInternalCode() + 1) != null ? trimToNull(rs.getString(mapping.getInternalCode() + 1)) : null)
				.bowl(mapping.getBowl() != null && rs.getString(mapping.getBowl() + 1) != null ? trimToNull(rs.getString(mapping.getBowl() + 1)) : null)
				.photo(mapping.getPhoto() != null && rs.getString(mapping.getPhoto() + 1) != null ? trimToNull(rs.getString(mapping.getPhoto() + 1)) : null)
				.unit(mapping.getUnit() != null && rs.getString(mapping.getUnit() + 1) != null ? trimToNull(rs.getString(mapping.getUnit() + 1)) : null)
				.build();
	}
	

}
