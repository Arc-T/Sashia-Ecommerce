import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/(app)/(authenticated)/dashboard/profile/')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/dashboard/profile/"!</div>
}
