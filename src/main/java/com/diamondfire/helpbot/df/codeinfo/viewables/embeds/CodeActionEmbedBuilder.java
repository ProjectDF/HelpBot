package com.diamondfire.helpbot.df.codeinfo.viewables.embeds;

import com.diamondfire.helpbot.df.codeinfo.codedatabase.db.datatypes.*;
import com.diamondfire.helpbot.df.codeinfo.viewables.BasicReaction;
import com.diamondfire.helpbot.bot.HelpBotInstance;
import com.diamondfire.helpbot.util.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;

import java.util.*;

public class CodeActionEmbedBuilder extends IconEmbedBuilder {

    @Override
    protected EmbedBuilder buildDataEmbed(SimpleData data) {

        CodeBlockActionData actionData = (CodeBlockActionData) data;

        EmbedBuilder builder = new EmbedBuilder();
        generateParameters(data, builder);
        generateInfo(data, builder);


        StringBuilder footer = new StringBuilder();
        if (actionData.getTags().length != 0) {
            footer.append(actionData.getTags().length)
                    .append(Util.sCheck(" Tag", actionData.getTags().length));
        }

        builder.setColor(actionData.getCodeBlockData().getCodeblockEnum().getColor());
        builder.setFooter(footer.toString());

        Emote emote = HelpBotInstance.getJda().getEmoteById(actionData.getCodeBlockData().getCodeblockEnum().getEmoji());
        builder.setAuthor(StringUtil.smartCaps(actionData.getCodeblockName()), null, emote.getImageUrl());

        return builder;


    }

    private void generateParameters(SimpleData data, EmbedBuilder builder) {
        StringBuilder params = new StringBuilder();
        DisplayIconData icon = data.getItem();

        for (CodeBlockActionArgumentData arg : icon.getParameters()) {
            params.append("\n");
            if (arg.getText() != null) {
                params.append(arg.getText());
                continue;
            }

            String line = String.format("**%s**", ParamConverter.getTypeFromString(arg.getType()).getText()) +
                    (arg.isPlural() ? "(s)" : "") +
                    (arg.isOptional() ? "*" : "") +
                    " - " + arg.getDescription()[0] + " "; // If there is a description on the argument, first part appears in same line.

            params.append(line);

            // Generate the description of the argument (skip the first index)
            String[] description = arg.getDescription();
            params.append(String.join(" ", Arrays.copyOfRange(description, 1, description.length)));

            // Generate extra notes that appear under the argument.
            params.append(StringUtil.listView( "> ", true, arg.getExtraNotes()));

        }
        if (icon.getParameters().length == 0) {
            params.append("(None)");
        }

        builder.addField("<:c_chest:688643661755318272> Parameters", params.toString(), false);

    }

    @Override
    public LinkedHashMap<BasicReaction, SimpleData> generateDupeEmojis(List<SimpleData> dataArrayList) {
        LinkedHashMap<BasicReaction, SimpleData> dataHashed = new LinkedHashMap<>();
        for (SimpleData data : dataArrayList) {
            CodeBlockActionData actionData = (CodeBlockActionData) data;
            dataHashed.put(new BasicReaction(actionData.getCodeBlockData().getCodeblockEnum().getEmoji()), data);
        }

        return dataHashed;
    }
}
