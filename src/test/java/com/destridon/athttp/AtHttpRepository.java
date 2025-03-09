package com.destridon.athttp;

import com.destridon.athttp.Annotations.*;

@HttpExchange(value = "/repos/{owner}/{repo}", accept = "application/vnd.github.v3+json")
public interface AtHttpRepository {

	String getRepository(@PathVariable String owner, @PathVariable String repo);

	void patchRepository(@PathVariable String owner, @PathVariable String repo, @RequestParam String name, @RequestParam String description, @RequestParam String homepage);

	@HttpExchange(value = "/sub")
	public static interface subInterface {
		
		String postResult();
		
	}

}
