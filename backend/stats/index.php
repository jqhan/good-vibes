<!DOCTYPE html>
<html>
<head>
<title>rocke stats</title>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <link href='https://fonts.googleapis.com/css?family=Playfair+Display' rel='stylesheet' type='text/css'>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/normalize/5.0.0/normalize.min.css">
  <link rel='stylesheet prefetch' href='http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css'>
  <link rel='stylesheet prefetch' href='http://cdnjs.cloudflare.com/ajax/libs/animate.css/3.2.3/animate.min.css'>

  <link rel="stylesheet" href="css/style.css">

  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
  <!-- Plotly.js -->
  <script src="https://cdn.plot.ly/plotly-latest.min.js"></script>

</head>
<body>
<div id='hero'>
  <div class='layer-bg layer' data-depth='0.10' data-type='parallax'></div>
  <div class='layer-1 layer' data-depth='0.20' data-type='parallax'></div>
  <div class='layer-2 layer' data-depth='0.50' data-type='parallax'></div>
  <div class='layer-3 layer' data-depth='0.80' data-type='parallax'></div>
  <div class='layer-overlay layer' data-depth='0.85' data-type='parallax'></div>
  <div class='layer-4 layer' data-depth='1.00' data-type='parallax'></div>
</div>
<div id='hero-mobile'></div>
<div id='content'>
  <div class='container'>
    <section class='first-section'>
      <div class='row'>
        <div class='col-sm-6'>
  		<div id="graph" style="width: 480px; height: 400px;"><!-- Plotly chart will be drawn inside this DIV --></div>
        </div>
        </div>
      </div>
    </section>
  </div>
</div>
  
    <script src="js/index.js"></script>
    <script>

    $(document).ready(function () {
	$.post("get-games.php", function (data) {

		console.log(data);
		var sesh_ids = [];
		var game_nums = [];
		var wins = [];
		var vibes = [];
		var playtilloses = [];
		var dates = [];

		for (var i in data) {
		    sesh_ids.push(data[i].sesh_id);
		    game_nums.push(data[i].game_num);
		    wins.push(data[i].win);
		    vibes.push(data[i].vibe);
		    playtilloses.push(data[i].playtillose);
		    dates.push(data[i].date);
		}

		var trace_wins = {
			x: game_nums,
			y: wins,
			type: 'scatter',
			name: 'wins',
			transforms: [{
			    type: 'aggregate',
			    groups: game_nums,
			    aggregations: [{
				target: 'y',
				func: 'avg',
				enabled: true
			    }]
			},{
			    type: 'sort',
			    target: 'x',
			    order: 'ascending'
			}]
		};

		var trace_vibe = {
			x: game_nums,
			y: vibes,
			type: 'scatter',
			name: 'vibe',
			transforms: [{
			    type: 'aggregate',
			    groups: game_nums,
			    aggregations: [{
				target: 'y',
				func: 'avg',
				enabled: true
			    }]
			},{
			    type: 'sort',
			    target: 'x',
			    order: 'ascending'
			}]
		};

		var trace_playtillose = {
			type: 'scatter',
			mode: 'markers',
			x: game_nums,
			y: playtilloses,
			name: 'Play \'til lose™',
			marker: {
					//pink
				color: 'rgba(255, 20, 147, 0.95)',
				line: {
				color: 'rgba(156, 165, 196, 1.0)',
				width: 1
				},
			    symbol: 'circle',
			    size: 4,
			},
			transforms: [{
			    type: 'aggregate',
			    groups: game_nums,
			    aggregations: [{
				target: 'y',
				func: 'avg',
				enabled: true
			    }]
			},{
			    type: 'sort',
			    target: 'x',
			    order: 'ascending'
			}]
		};

		var layout = {
			yaxis2: {
			    domain: [0.6, 0.95], 
			    anchor: 'x2'
			  }, 
			xaxis2: {
			    domain: [0.6, 0.95], 
			    anchor: 'y2'
			  }
		};

		var traces = [trace_wins, trace_vibe, trace_playtillose];

		Plotly.plot('graph', traces);

	}).done(function(success) {
		console.log("SUCCESS!");
	}).fail(function(xhr, textStatus, error) {
		console.log("ERROR!");
		console.log(xhr.statusText);
		console.log(textStatus);
		console.log(error);
	});
    });
</script>
</body>
</html>
