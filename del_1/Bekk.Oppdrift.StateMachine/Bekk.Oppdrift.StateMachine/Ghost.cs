namespace Bekk.Oppdrift.StateMachine;

public class Ghost
{
    public GhostState State { get; private set; }
    public void SetState(Event @event)
    {
        switch (State)
        {
            case GhostState.WalkTheMaze:
                State = @event switch
                {
                    Event.DiscoverPacMan => GhostState.PursuePacMan,
                    Event.PacManEatsPellet => GhostState.RunAwayFromPacMan,
                    _ => State
                };
                break;
            case GhostState.PursuePacMan:
                State = @event switch
                {
                    Event.LoseTrackOfPacMan => GhostState.WalkTheMaze,
                    Event.PacManEatsPellet => GhostState.RunAwayFromPacMan,
                    _ => State
                };
                break;
            case GhostState.GoBackToBase:
                State = @event switch
                {
                    Event.ArrivedAtBase => GhostState.WalkTheMaze,
                    _ => State
                };
                break;
            case GhostState.RunAwayFromPacMan:
                State = @event switch
                {
                    Event.EatenByPacMan => GhostState.GoBackToBase,
                    Event.PelletStopsWorking => GhostState.WalkTheMaze,
                    _ => State
                };
                break;
        }
    }
    public override string ToString() => State.ToString();
}