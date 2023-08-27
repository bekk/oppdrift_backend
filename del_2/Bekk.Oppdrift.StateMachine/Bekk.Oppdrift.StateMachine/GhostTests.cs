using Xunit;

namespace Bekk.Oppdrift.StateMachine;

public class GhostTests
{
    [Theory]
    [InlineData(GhostState.PursuePacMan, Event.DiscoverPacMan)]
    [InlineData(GhostState.WalkTheMaze, Event.DiscoverPacMan, Event.LoseTrackOfPacMan)]
    [InlineData(GhostState.WalkTheMaze, Event.DiscoverPacMan, Event.PelletStopsWorking, Event.LoseTrackOfPacMan)]
    [InlineData(GhostState.GoBackToBase, Event.PacManEatsPellet, Event.DiscoverPacMan, Event.EatenByPacMan)]
    [InlineData(GhostState.WalkTheMaze, Event.PacManEatsPellet, Event.EatenByPacMan, Event.ArrivedAtBase)]
    public void SetState_WithEvent_SetsNewState(GhostState expectedState, params Event[] events)
    {
        var target = new Ghost();
        foreach (var @event in events)
        {
            target.SetState(@event);
        }
        
        Assert.Equal(expectedState, target.State);
    }
}