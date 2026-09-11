import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/(app)/(authenticated)/dashboard/settings/')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/dashboard/settings/"!</div>
}
