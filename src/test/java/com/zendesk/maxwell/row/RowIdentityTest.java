package com.zendesk.maxwell.row;


import com.google.common.collect.Lists;
import com.zendesk.maxwell.MaxwellTestJSON;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

public class RowIdentityTest {

	@Test
	public void testToJson() throws IOException {
		RowIdentity rowId = new RowIdentity("MyDatabase", "MyTable",
			RowIdentity.pairs(RowIdentity.pair("id", 111), RowIdentity.pair("account", 123)));

		String jsonHash = rowId.toKeyJson(RowMap.KeyFormat.HASH);
		Assert.assertEquals("{\"database\":\"MyDatabase\",\"table\":\"MyTable\",\"pk.id\":111,\"pk.account\":123}", jsonHash);

		String jsonArray = rowId.toKeyJson(RowMap.KeyFormat.ARRAY);
		Assert.assertEquals("[\"MyDatabase\",\"MyTable\",[{\"id\":111},{\"account\":123}]]", jsonArray);
	}

	@Test
	public void testPkToJsonArrayWithListData() throws Exception {
		RowIdentity rowId = new RowIdentity("MyDatabase", "MyTable",
			RowIdentity.pairs(RowIdentity.pair("id", "9001"), RowIdentity.pair("name", Lists.newArrayList("example"))));

		String jsonString = rowId.toKeyJson(RowMap.KeyFormat.ARRAY);

		Assert.assertEquals("[\"MyDatabase\",\"MyTable\",[{\"id\":\"9001\"},{\"name\":[\"example\"]}]]",
			jsonString);
	}

	@Test
	public void testPkToJsonHashWithEmptyData() throws Exception {
		RowIdentity rowId = new RowIdentity("MyDatabase", "MyTable", RowIdentity.pairs());

		String jsonString = rowId.toKeyJson(RowMap.KeyFormat.HASH);

		Map<String, Object> jsonMap = MaxwellTestJSON.parseJSON(jsonString);

		Assert.assertTrue(jsonMap.containsKey("_uuid"));
		Assert.assertEquals("MyDatabase", jsonMap.get("database"));
		Assert.assertEquals("MyTable", jsonMap.get("table"));
	}
}
