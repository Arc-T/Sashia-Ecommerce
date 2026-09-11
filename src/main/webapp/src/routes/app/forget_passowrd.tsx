import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/(app)/(anonymous)/forget_passowrd')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/(authentication)/forget_passowrd"!</div>
}
