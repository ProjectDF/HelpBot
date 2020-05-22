package com.owen1212055.helpbot.events;

import com.owen1212055.helpbot.components.reactions.ReactionHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class ReactionEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
        if (event.getMember().getUser().isBot()) {
            return;
        }
        if (ReactionHandler.isMessageReserved(event.getMessageIdLong())) {
            Message message = null;
            try {
                message = event.getChannel().retrieveMessageById(event.getMessageIdLong()).complete(true);
            } catch (RateLimitedException e) {
                e.printStackTrace();
            }

            MessageReaction reaction = message.getReactions().stream()
                    .filter((messageReaction) -> messageReaction.getReactionEmote().equals(event.getReaction().getReactionEmote()) && messageReaction.getCount() != 2)
                    .findFirst()
                    .orElse(null);

            if (reaction != null) {
                if (event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_MANAGE)) {
                    MessageReaction.ReactionEmote emote = reaction.getReactionEmote();
                    if (emote.isEmoji()) {
                        message.removeReaction(emote.getEmoji(), event.getUser()).queue();
                    } else if (emote.isEmote()) {
                        message.removeReaction(emote.getEmote(), event.getUser()).queue();
                    }
                    return;

                }
            }

        }

        if (ReactionHandler.isWaiting(event.getMember().getIdLong())) {
            ReactionHandler.reacted(event.getMember().getIdLong(), event.getReaction().getMessageIdLong(), event.getReaction());
        }

    }
}