// Widget that shows a simple text result and updates it when new text comes in

function ServerStatsWidget(resultId) {
    this.resultId = resultId;
}

ServerStatsWidget.constuctWidget = function(resultId) {
    console.log("Creating server stats result widget with ID: " + resultId);
    return new ServerStatsWidget(resultId);
};

ServerStatsWidget.prototype.createWithData = function(data) {
    console.log("Creating result with data: " + data);
    var currentInfo = this.createCurrentInfo(data);
    $('#' + this.resultId).text(currentInfo);
};

ServerStatsWidget.prototype.updateWithData = function(data) {
    console.log("Updating result with data: " + data);
    var currentInfo = this.createCurrentInfo(data);
    $('#' + this.resultId).text(currentInfo);
};

ServerStatsWidget.prototype.createCurrentInfo = function(data) {
    var info = "Uptime: " + data.uptime + "Current heap usage: " + data.heapUsage + " ("+ data.heapPercentageUsage + ")";
    return info;
};

FeedResultManager.registerWidgetConstructor("SERVER_STATS_RESULT", ServerStatsWidget.constuctWidget);