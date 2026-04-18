package me.colingrimes.tweaky;

import me.colingrimes.tweaky.message.MessageService;
import me.colingrimes.tweaky.message.PaperMessageService;

import javax.annotation.Nonnull;

public class TweakyPaper extends Tweaky {

	@Nonnull
	@Override
	public MessageService getMessageService() {
		return new PaperMessageService();
	}

	@Nonnull
	@Override
	public String getVersion() {
		return "Tweaky-" + getDescription().getVersion() + "-paper";
	}

	@Override
	public boolean isPaper() {
		return true;
	}
}
