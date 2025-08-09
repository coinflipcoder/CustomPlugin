package de.coinflipcoder.customplugin.events;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.status.server.WrapperStatusServerResponse;
import com.google.gson.JsonObject;
import com.velocitypowered.api.event.Subscribe;

public class SafeServerPacket extends PacketListenerAbstract {

    @Subscribe
    public void onPacketSend(com.github.retrooper.packetevents.event.PacketSendEvent event) {
        if (!(event.getPacketType() == PacketType.Status.Server.RESPONSE)) return;
        WrapperStatusServerResponse response = new WrapperStatusServerResponse(event);
        JsonObject json = response.getComponent();
        json.addProperty("preventsChatReports", true);
        response.setComponent(json);
        event.markForReEncode(true);
    }
}
